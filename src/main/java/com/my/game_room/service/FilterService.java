package com.my.game_room.service;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;
import com.my.game_room.model.toy.AbstractToy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for processing business object
 * with filtering and sorting functionality.
 */
public class FilterService {
    private static final Logger LOG = LogManager.getLogger();
    private static FilterService filterService;

    private FilterService() {
    }

    public static FilterService getInstance() {
        if (filterService == null) {
            filterService = new FilterService();
        }
        return filterService;
    }

    /**
     * filtering toys from input list by passed toy type state.
     *
     * @param list    of toys to filter
     * @param toyType type of toy
     * @return list of filtered toys
     */
    public List<AbstractToy> filterByType(List<AbstractToy> list, ToyType toyType) {
        LOG.info("Фільтруємо за типом = {}", toyType);
        return list.stream().filter(e -> e.getToyType().equals(toyType)).collect(Collectors.toList());
    }

    /**
     * filter by price
     *
     * @param list         of toys
     * @param pivotPrice   pivot price for toys in list
     * @param isDescending define if filtered list is decreasing or increasing
     * @return list of filtered toys
     */
    public List<AbstractToy> filterByPrice(List<AbstractToy> list, double pivotPrice, boolean isDescending) {
        if (isDescending) {
            LOG.info("Фільтруємо за ціною нижче ніж = {}", pivotPrice);
            return list.stream().filter(e -> e.calcPrice() <= pivotPrice).collect(Collectors.toList());
        } else {
            LOG.info("Фільтруємо за ціною вище ніж = {}", pivotPrice);
            return list.stream().filter(e -> e.calcPrice() >= pivotPrice).collect(Collectors.toList());
        }
    }

    /**
     * filter by size
     *
     * @param list of toys
     * @param size allowed size of toy
     * @return list of filtered toys
     */
    public List<AbstractToy> filterBySize(List<AbstractToy> list, SizeType size) {
        LOG.info("Фільтруємо за розміром = {}", size);
        return list.stream().filter(e -> size.equals(e.getSize())).collect(Collectors.toList());
    }

    /**
     * filter by age
     *
     * @param list list of toys
     * @param ageCategory age of toy in result list
     * @return list of filtered toys
     */
    public List<AbstractToy> filterByAge(List<AbstractToy> list, AgeCategory ageCategory) {
        LOG.info("Фільтруємо за доступним віком = {}", ageCategory);
        return list.stream().filter(e -> ageCategory.equals(e.getAge())).collect(Collectors.toList());
    }

    /**
     * sorting by price
     *
     * @param list list of toys
     * @param isDescending define if filtered list is decreasing or increasing
     * @return sorted list by price
     */
    public List<AbstractToy> sortByPrice(List<AbstractToy> list, boolean isDescending) {
        if (isDescending) {
            LOG.info("Сортуємо за спаданням ціни");
            return list.stream().sorted(Comparator.comparingDouble(AbstractToy::calcPrice).reversed()).collect(Collectors.toList());
        }
        LOG.info("Сортуємо за зростанням ціни");
        return list.stream().sorted(Comparator.comparingDouble(AbstractToy::calcPrice)).collect(Collectors.toList());
    }

    /**
     * sorting by size
     *
     * @param list list of toys
     * @param isDescending define if filtered list is decreasing or increasing
     * @return sorted list by price
     */
    public List<AbstractToy> sortBySize(List<AbstractToy> list, boolean isDescending) {
        if (isDescending) {
            LOG.info("Сортуємо за спаданням розміру");
            return list.stream().sorted(Comparator.comparing(AbstractToy::getSize).reversed()).collect(Collectors.toList());
        }
        LOG.info("Сортуємо за зростанням розміру");
        return list.stream().sorted(Comparator.comparing(AbstractToy::getSize)).collect(Collectors.toList());
    }

    /**
     * sorting by age
     *
     * @param list list of toys
     * @param isDescending define if filtered list is decreasing or increasing
     * @return sorted list by price
     */
    public List<AbstractToy> sortByAge(List<AbstractToy> list, boolean isDescending) {
        if (isDescending) {
            LOG.info("Сортуємо за спаданням вікової категорії");
            return list.stream().sorted(Comparator.comparing(AbstractToy::getAge).reversed()).collect(Collectors.toList());
        }
        LOG.info("Сортуємо за зростанням вікової категорії");
        return list.stream().sorted(Comparator.comparing(AbstractToy::getAge)).collect(Collectors.toList());
    }
}