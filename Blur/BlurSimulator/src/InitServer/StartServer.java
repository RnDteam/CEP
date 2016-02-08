package InitServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import DBHandler.DBReader;
import EventsHandler.InitializationBlurEvents;
import EventsHandler.SimulationBlurEvents;

import com.ibm.ia.common.DataFormat;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.gateway.GridConfiguration;
import com.ibm.ia.gateway.GridConnection;
import com.ibm.ia.gateway.GridConnectionFactory;
import com.ibm.ia.gateway.SolutionChangedException;
import com.ibm.ia.gateway.SolutionGateway;
import com.ibm.ia.gateway.client.GatewayClient;
import com.ibm.ia.model.Event;
import com.ibm.ia.testdriver.TestDriver;

public class StartServer {

	private static TestDriver testDriver = new TestDriver();
	private static final long SPEED_FACTOR = 200;
	private static AtomicInteger count = new AtomicInteger(0);

	public static void main(String[] args) {
		System.out.println("starting sever");
		startServer();
		System.out.println("shutting down...");
		System.exit(0);
	}

	private static void startServer() {
		try {
			testDriver.connect();
			testDriver.deleteAllEntities();
			testDriver.resetSolutionState();
			Thread.sleep(5000);
			// testDriver.startRecording();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		GridConnection connection = null;
		try {
			GatewayClient.init();
			GridConfiguration gridConfig = new GridConfiguration(
					"localhost:2809");

			connection = GridConnectionFactory.createGridConnection(gridConfig);
			SolutionGateway gateway = connection.getSolutionGateway("Blur");

			getDataAndSendEvents(gridConfig, gateway);

		} catch (Exception e) {
			System.out.println("Failed!");
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out
				.println("Waiting for solution idle so we can stop the recording.");
		testDriver.waitUntilSolutionIdle();
		// testDriver.stopRecording();
		testDriver.disconnect();

		System.out.println("finished!");
	}

	public static void getDataAndSendEvents(GridConfiguration config,
			SolutionGateway gateway) throws SolutionChangedException,
			GatewayException, RoutingException, SolutionException,
			UnsupportedOperationException, IOException {

		HashMap<ZonedDateTime, List<Event>> initEventsMap = new HashMap<>();
		addInitializationEventsToMap(initEventsMap, gateway);
		submitEventsAsync(initEventsMap, config, false);

		System.out
				.println("Waiting for solution to process initialization events.");
		testDriver.waitUntilSolutionIdle(20);

		System.out.println("Sending simulation events.");
		HashMap<ZonedDateTime, List<Event>> simulationEventsMap = new HashMap<>();
		addSimulationEventsToMap(simulationEventsMap, gateway);
		submitEventsAsync(simulationEventsMap, config, true);
	}

	private static void addInitializationEventsToMap(
			HashMap<ZonedDateTime, List<Event>> eventsMap,
			SolutionGateway gateway) {
		for (InitializationBlurEvents event : InitializationBlurEvents.values()) {
			List<Event> allEventInstances = event.getEventCreation()
					.getAllEntities(gateway);

			for (Event currEvent : allEventInstances) {
				ZonedDateTime timestamp = currEvent.get$Timestamp();

				if (eventsMap.get(timestamp) == null) {
					eventsMap.put(timestamp, new ArrayList<Event>());
				}

				eventsMap.get(timestamp).add(currEvent);
			}
		}
	}

	private static void addSimulationEventsToMap(
			HashMap<ZonedDateTime, List<Event>> eventsMap,
			SolutionGateway gateway) {
		for (SimulationBlurEvents event : SimulationBlurEvents.values()) {
			List<Event> allEventInstances = event.getEventCreation()
					.getAllEntities(gateway);

			for (Event currEvent : allEventInstances) {
				ZonedDateTime timestamp = currEvent.get$Timestamp();

				if (eventsMap.get(timestamp) == null) {
					eventsMap.put(timestamp, new ArrayList<Event>());
				}

				eventsMap.get(timestamp).add(currEvent);
			}
		}

		DBReader.closeConnection();
	}

	private static void submitEventsAsync(
			HashMap<ZonedDateTime, List<Event>> eventsMap,
			final GridConfiguration config, boolean sleep)
			throws SolutionException, UnsupportedOperationException,
			GatewayException, IOException {

		List<GridConnection> connections = Collections
				.synchronizedList(new ArrayList<>());
		ExecutorService executor = Executors.newFixedThreadPool(4);
		ThreadLocal<SolutionGateway> gateway = new ThreadLocal<SolutionGateway>() {

			@Override
			protected SolutionGateway initialValue() {
				try {
					GridConnection connection = GridConnectionFactory
							.createGridConnection(config);
					connections.add(connection);
					return connection.getSolutionGateway("Blur");
				} catch (GatewayException e) {
					throw new RuntimeException(e);
				}
			}

		};

		Set<ZonedDateTime> keySet = eventsMap.keySet();
		ArrayList<ZonedDateTime> sortedEvents = new ArrayList<ZonedDateTime>(
				keySet);
		Collections.sort(sortedEvents);

		ZonedDateTime lastTimeStamp = sortedEvents.get(0);

		for (ZonedDateTime currTime : sortedEvents) {
			for (Event eventToSubmit : eventsMap.get(currTime)) {
				saveXml(eventToSubmit, count.incrementAndGet());
				executor.execute(new SubmitEvent(gateway.get(), eventToSubmit));
			}

			if (sleep) {
				try {
					long sleepTime = calculateDiffernceBetweenDates(currTime,
							lastTimeStamp) * SPEED_FACTOR;
					System.out.println("Sleeping for ms: " + sleepTime);
					Thread.sleep(sleepTime);

					lastTimeStamp = currTime;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		executor.shutdown();

		try {
			if (executor.awaitTermination(2, TimeUnit.MINUTES)) {
				closeAllConnections(connections);
			}
		} catch (InterruptedException e) {
			closeAllConnections(connections);
		}
	}

	private static void saveXml(Event event, int count) throws IOException,
			SolutionException, UnsupportedOperationException, GatewayException {

		// save event locally before we submit it
		File theDir = new File("./events/");

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			theDir.mkdir();
		}

		String countAsString = String.format("%05d", count);
		OutputStream outStream = new FileOutputStream("./events/"
				+ countAsString + "_" + event.get$TypeName() + ".xml");
		testDriver.getModelSerializer().serializeEvent(DataFormat.GENERIC_XML,
				outStream, event, "UTF-8");
		outStream.close();
	}

	private static void closeAllConnections(List<GridConnection> connections) {
		for (GridConnection connection : connections) {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static long calculateDiffernceBetweenDates(ZonedDateTime currTime,
			ZonedDateTime lastTimeStamp) {

		long result = ChronoUnit.MINUTES.between(
				lastTimeStamp.toLocalDateTime(), currTime.toLocalDateTime());

		if (result > 30) {
			System.out
					.println("WARNIG: minutes between current event and last event is more than 30: "
							+ result);
			result = 30;
		}

		return result;
	}
}
