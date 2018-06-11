package marshallers.interfaces;

public interface Marshaller {

    public <E> void marshall(E entity, String fileName);
    public <E> E unmarshall(Class<E> classToken, String fileName);
}
