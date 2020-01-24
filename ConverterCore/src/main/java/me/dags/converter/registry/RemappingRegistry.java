package me.dags.converter.registry;

import me.dags.converter.util.log.Logger;

import java.util.Iterator;

public class RemappingRegistry<T extends RegistryItem> implements Registry<T> {

    private final Registry<T> registry;
    private final Registry.Mapper<T> mapper;

    public RemappingRegistry(Registry<T> registry, Mapper<T> mapper) {
        this.registry = registry;
        this.mapper = mapper;
    }

    @Override
    public int size() {
        return registry.size();
    }

    @Override
    public String getVersion() {
        return mapper.getVersion();
    }

    @Override
    public T getValue(int id) {
        return getOutput(getInput(id));
    }

    @Override
    public T getInput(int id) {
        T source = registry.getValue(id);
        if (registry.isDefault(source)) {
            Logger.log(new RuntimeException("Missing value for id: " + registry.getIdentifier(source))).flush();
        }
        return source;
    }

    @Override
    public T getOutput(T input) {
        return mapper.apply(input);
    }

    @Override
    public int getId(T val) {
        return val.getId();
    }

    @Override
    public Parser<T> getParser() {
        return registry.getParser();
    }

    @Override
    public Iterator<T> iterator() {
        return registry.iterator();
    }
}
