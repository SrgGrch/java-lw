package model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientsMessage {
    public static final int TYPE = 3;

    private final int SIZE;

    private final List<UUID> ids;


    public ClientsMessage(List<UUID> ids) {
        this.ids = ids;

        SIZE = Integer.BYTES + Integer.BYTES + ids.size() * 2 * Long.BYTES;
    }

    public ClientsMessage(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        List<UUID> ids = new ArrayList<>();
        int size = buffer.getInt();
        for (int i = 0; i < size; i++) {
            ids.add(new UUID(buffer.getLong(), buffer.getLong()));
        }

        SIZE = Integer.BYTES + Integer.BYTES + ids.size() * 2 * Long.BYTES;
        this.ids = ids;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(SIZE);

        buffer.putInt(TYPE);
        buffer.putInt(ids.size());

        ids.forEach(id -> {
            buffer.putLong(id.getMostSignificantBits());
            buffer.putLong(id.getLeastSignificantBits());
        });

        return buffer.array();
    }

    public List<UUID> getIds() {
        return ids;
    }
}
