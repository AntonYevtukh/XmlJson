package serializers.interfaces;

import java.io.IOException;

public interface Serializer<E> {

    String serialize(E entity) throws IOException;
    E deserialize(Class<E> classToken, String fileBody) throws IOException;
}
