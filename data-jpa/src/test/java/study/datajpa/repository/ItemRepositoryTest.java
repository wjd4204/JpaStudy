package study.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @DisplayName("아이템을 저장합니다.")
    @Test
    void save(){
     //given
        Item item = new Item("A");
        itemRepository.save(item);
        // save 자체에 이미 transactional이 저장되어 있음.

     //when

     //then
    }

}