package com.fastcampus.project02.infomanagement.repository;

import com.fastcampus.project02.infomanagement.domain.Block;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@SpringBootTest
class BlockRepositoryTest {
    @Autowired
    private BlockRepository blockRepository;

    @Test
    @Transactional
    void crud(){
        Block block = new Block();
        block.setName("허준");
        block.setReason("친하지않음");
        block.setStartDate(LocalDate.now());
        block.setEndDate(LocalDate.now());

        blockRepository.save(block);

        List<Block> blockList = blockRepository.findAll();

        Assertions.assertEquals(3, blockList.size());
        Assertions.assertEquals("허준", blockList.get(2).getName());
    }
}