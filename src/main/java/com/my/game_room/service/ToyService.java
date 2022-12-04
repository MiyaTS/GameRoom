package com.my.game_room.service;

import com.my.game_room.dao.Dao;
import com.my.game_room.dao.ToyDao;
import com.my.game_room.model.toy.AbstractToy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * toy service class
 * <p>
 * implement CRUD operations for Toys business objects and its states.
 * <p>
 * receiver class
 */
public class ToyService {
    private static final Logger LOG = LogManager.getLogger();
    private static ToyService toyService;
    private final FilterService filterService;
    private Dao<AbstractToy> toyDao;
    private List<AbstractToy> toys;
    private AbstractToy toy;

    private ToyService() {
        filterService = FilterService.getInstance();
        toyDao = new ToyDao();
    }

    public static ToyService getInstance() {
        if (toyService == null) {
            toyService = new ToyService();
        }
        return toyService;
    }

    /**
     * Return toy instance from cache. Clear cache memory.
     *
     * @return toy
     */
    public AbstractToy getSelectedToy() {
        var selectedToy = toy;
        toy = null;
        return selectedToy;
    }

    /**
     * Read toy from memory and return available instance.
     *
     * @param index of toy in memory list
     * @return Optional of toy or empty state if not available
     */
    public Optional<AbstractToy> getToyFromStore(int index) {
        LOG.info("Зчитування іграшки з пам'яті по індексу {}", index);
        if (!validateIndex(index)) return Optional.empty();
        return Optional.of(toys.get(index));
    }

    /**
     * sorting toys by price
     *
     * @param isDescending define if filtered list is decreasing or increasing
     * @return sorted list
     */
    public List<AbstractToy> sortToysByPrice(boolean isDescending) {
        var list = viewAllToys();
        if (list.isEmpty()) {
            LOG.info("Список іграшок пустий");
            return list;
        }
        toys = filterService.sortByPrice(list, isDescending);
        return toys;
    }

    /**
     * sorting toys by size
     *
     * @param isDescending define if filtered list is decreasing or increasing
     * @return sorted list
     */
    public List<AbstractToy> sortToysBySize(boolean isDescending) {
        var list = viewAllToys();
        if (list.isEmpty()) {
            LOG.info("Список іграшок пустий");
            return list;
        }
        toys = filterService.sortBySize(list, isDescending);
        return toys;
    }

    /**
     * sorting toys by age
     *
     * @param isDescending define if filtered list is decreasing or increasing
     * @return sorted list
     */
    public List<AbstractToy> sortToysByAge(boolean isDescending) {
        var list = viewAllToys();
        if (list.isEmpty()) {
            LOG.info("Список іграшок пустий");
            return list;
        }
        toys = filterService.sortByAge(list, isDescending);
        return toys;
    }

    /**
     * Toy creation editor
     * creating a toy depending on the type
     * saving the toy to the database
     *
     * @param toy instance of some of AbstractToy child class
     * @return true if toy was saved in database
     */
    public boolean create(AbstractToy toy) {
        return toyDao.save(toy);
    }

    /**
     * Update toy instance in database
     *
     * @param toy instance from controllers
     * @return true if toy was update successfully
     */
    public boolean edit(AbstractToy toy) {
        if (toy == null) return false;
        return toyDao.update(toy);
    }

    /**
     * Deleting toy from database and cache by index
     *
     * @param index of toy in memory
     * @return true if toy was deleted
     */
    public boolean delete(int index) {
        LOG.info("Видалення іграшки {}", index);
        if (!validateIndex(index)) return false;
        var id = toys.get(index).getId();
        toys.remove(index);
        return toyDao.delete(id);
    }

    /**
     * Verify index against available list of toys in memory
     *
     * @param index of room in cache
     * @return true if index is valid
     */
    private boolean validateIndex(int index) {
        if (index < 0 || index >= toys.size()) {
            LOG.warn("Неправильний індекс");
            return false;
        }
        return true;
    }

    /**
     * view info about toy,
     * saving toy into cache
     *
     * @param index toy id
     * @return value of toy
     */
    public Optional<AbstractToy> viewInfo(int index) {
        if (!validateIndex(index)) return Optional.empty();
        toy = toys.get(index);
        return Optional.of(toy);
    }

    /**
     * outputs all toys from the database
     *
     * @return true if this list contains no elements
     */
    public List<AbstractToy> viewAllToys() {
        LOG.info("Виведення списку іграшок");
        toys = toyDao.getAll();
        return toys;
    }

    public void setToyDao(Dao<AbstractToy> toyDao) {
        this.toyDao = toyDao;
    }
}
