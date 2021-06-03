import model.ClientsMessage;
import model.NewClientMessage;
import model.SendCarsRequest;
import model.SendCarsResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class SocketServer {
    ServerSocket server = new ServerSocket(1984);

    public static void main(String[] args) {
        try {
            SocketServer socketServer = new SocketServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SocketServer() throws IOException {
        Map<UUID, Socket> sockets = new HashMap<>();

        Thread socketThread = new Thread(() -> {
            while (true) {
                try {
                    Socket newSocket = server.accept();
                    UUID newSocketId = UUID.randomUUID();

                    try {
                        OutputStream os = newSocket.getOutputStream();
                        os.write(new ClientsMessage(new ArrayList<>(sockets.keySet())).getBytes());
                        os.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        synchronized (sockets) {
                            sockets.remove(newSocketId);
                        }
                    }

                    synchronized (sockets) {
                        sockets.put(newSocketId, newSocket);
                    }

                    synchronized (sockets) {
                        sockets.forEach((uuid, socket) -> {
                            try {
                                if (uuid != newSocketId) {
                                    OutputStream os = socket.getOutputStream();
                                    os.write(new NewClientMessage(newSocketId).getBytes());
                                    os.flush();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                synchronized (sockets) {
                                    sockets.remove(uuid);
                                }
                            }
                        });
                    }

                    System.out.println("Accepted " + newSocketId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        socketThread.start();

        while (true) {
            synchronized (sockets) {
                sockets.forEach((uuid, socket) -> {
                    System.out.println(uuid);
                    try {
                        BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
                        List<Byte> byteList = new ArrayList<>();
                        while (reader.available() > 0) {
                            byteList.add((byte) reader.read());
                        }

                        if (byteList.size() != 0) {
                            byte[] bytes = new byte[byteList.size()];
                            for (int i = 0; i < byteList.size(); i++) {
                                bytes[i] = byteList.get(i);
                            }

                            SendCarsRequest request = new SendCarsRequest(bytes);

                            SendCarsResponse response = new SendCarsResponse(request.getCars());

                            Socket receiver = sockets.get(request.getReceiver());
                            OutputStream os = receiver.getOutputStream();
                            os.write(response.getBytes());
                            os.flush();

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        synchronized (sockets) {
                            sockets.remove(uuid);
                        }
                    }
                });
            }
        }
    }
}
