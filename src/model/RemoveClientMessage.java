package model;

import java.nio.ByteBuffer;
import java.util.UUID;

public class RemoveClientMessage {
    public static final int TYPE = 2;

    private static final int SIZE = Integer.BYTES + Long.BYTES * 2;

    private final UUID id;


    public RemoveClientMessage(UUID id) {
        this.id = id;
    }

    public RemoveClientMessage(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        this.id = new UUID(buffer.getLong(), buffer.getLong());
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(SIZE);

        buffer.putInt(TYPE);
        buffer.putLong(id.getMostSignificantBits());
        buffer.putLong(id.getLeastSignificantBits());

        return buffer.array();
    }

    public UUID getId() {
        return id;
    }
}
