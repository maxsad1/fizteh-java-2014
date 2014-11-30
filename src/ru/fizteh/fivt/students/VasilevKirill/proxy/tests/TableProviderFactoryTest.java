package ru.fizteh.fivt.students.VasilevKirill.proxy.tests;

import org.junit.Test;
import ru.fizteh.fivt.storage.structured.TableProviderFactory;
import ru.fizteh.fivt.students.VasilevKirill.Storeable.junit.MyTableProviderFactory;

import static org.junit.Assert.assertTrue;

public class TableProviderFactoryTest {
    private static TableProviderFactory factory;

    static {
        factory = new MyTableProviderFactory();
    }

    @Test
    public void testCreate() throws Exception {
        try {
            factory.create(null);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}
