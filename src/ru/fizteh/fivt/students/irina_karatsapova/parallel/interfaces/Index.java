package ru.fizteh.fivt.students.irina_karatsapova.parallel.interfaces;

/**
 * Представляет базу для интерфейс для работы с таблицей, содержащей ключи-значения.
 * Используется также для доступа к поисковому индексу.
 */
public interface Index {
    /**
     * Возвращает название таблицы или индекса.
     *
     * @return Название таблицы.
     */
    String getName();

    /**
     * Получает значение по указанному ключу.
     *
     * @param key Ключ для поиска значения. Не может быть null.
     *            Для индексов по не-строковым полям аргумент представляет собой сериализованное значение колонки.
     *            Его потребуется распарсить.
     * @return Значение. Если не найдено, возвращает null.
     *
     * @throws IllegalArgumentException Если значение параметра key является null.
     */
    Storeable get(String key);
}
