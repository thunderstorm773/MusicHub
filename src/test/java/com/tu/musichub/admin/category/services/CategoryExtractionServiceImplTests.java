package com.tu.musichub.admin.category.services;

import com.tu.musichub.admin.category.entities.Category;
import com.tu.musichub.admin.category.models.views.CategoryView;
import com.tu.musichub.admin.category.repositories.CategoryRepository;
import com.tu.musichub.staticData.TestConstants;
import com.tu.musichub.util.MapperUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles(TestConstants.TEST_PROFILE)
@SpringBootTest
public class CategoryExtractionServiceImplTests {

    private static final String NON_EXISTENT_CATEGORY_NAME = "Rock";
    private static final String EXISTENT_CATEGORY_NAME = "Pop";
    private static final Long EXISTENT_CATEGORY_ID = 1L;
    private static final Long NON_EXISTENT_CATEGORY_ID = 5L;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private CategoryExtractionService categoryExtractionService;

    @Mock
    private Pageable pageable;

    private Category createCategory() {
        Category category = new Category();
        category.setId(EXISTENT_CATEGORY_ID);
        category.setName(EXISTENT_CATEGORY_NAME);
        return category;
    }

    private Page<Category> createCategoryPage() {
        List<Category> categories = new ArrayList<>();
        categories.add(this.createCategory());
        return new PageImpl<>(categories);
    }

    @Before
    public void setUp() {
        Mockito.when(this.categoryRepository.findByName(NON_EXISTENT_CATEGORY_NAME))
                .thenReturn(null);

        Mockito.when(this.categoryRepository.findByName(EXISTENT_CATEGORY_NAME))
                .thenReturn(this.createCategory());

        Mockito.when(this.categoryRepository.findAll(this.pageable))
                .thenReturn(this.createCategoryPage());

        Mockito.when(this.categoryRepository.findAll())
                .thenReturn(new ArrayList<>());

        Mockito.when(this.categoryRepository.findById(NON_EXISTENT_CATEGORY_ID))
                .thenReturn(Optional.empty());

        Mockito.when(this.categoryRepository.findById(EXISTENT_CATEGORY_ID))
                .thenReturn(Optional.of(this.createCategory()));
    }

    @Test
    public void testFindByName_withNonExistentCategoryName_returnsNull() {
        CategoryView categoryView = this.categoryExtractionService
                .findByName(NON_EXISTENT_CATEGORY_NAME);

        Assert.assertNull(categoryView);
    }

    @Test
    public void testFindByName_withExistentCategoryName_returnsCorrectlyMappedCategoryView() {
        CategoryView categoryView = this.categoryExtractionService
                .findByName(EXISTENT_CATEGORY_NAME);

        Assert.assertEquals(EXISTENT_CATEGORY_ID, categoryView.getId());
        Assert.assertEquals(EXISTENT_CATEGORY_NAME, categoryView.getName());
    }

    @Test
    public void testFindByName_withExistentCategoryName_shouldInvokeCategoryRepositoryFindByName() {
        this.categoryExtractionService.findByName(EXISTENT_CATEGORY_NAME);

        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .findByName(EXISTENT_CATEGORY_NAME);
    }

    @Test
    public void testFindByName_withNonExistentCategoryName_shouldInvokeCategoryRepositoryFindByName() {
        this.categoryExtractionService.findByName(NON_EXISTENT_CATEGORY_NAME);

        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .findByName(NON_EXISTENT_CATEGORY_NAME);
    }

    @Test
    public void testFindAll_withPageable_shouldInvokeCategoryRepositoryFindAllWithPageable() {
        this.categoryExtractionService.findAll(this.pageable);
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .findAll(this.pageable);
    }

    @Test
    public void testFindAll_shouldInvokeCategoryRepository() {
        this.categoryExtractionService.findAll();
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    public void testFindById_withNonExistentCategoryId_shouldInvokeCategoryRepositoryFindById() {
        this.categoryExtractionService.findById(NON_EXISTENT_CATEGORY_ID);
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .findById(NON_EXISTENT_CATEGORY_ID);
    }

    @Test
    public void testFindById_withExistentCategoryId_shouldInvokeCategoryRepositoryFindById() {
        this.categoryExtractionService.findById(EXISTENT_CATEGORY_ID);
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .findById(EXISTENT_CATEGORY_ID);
    }

    @Test
    public void testFindById_withNonExistentCategoryId_returnsNull() {
        CategoryView categoryView = this.categoryExtractionService
                .findById(NON_EXISTENT_CATEGORY_ID);
        Assert.assertNull(categoryView);
    }

    @Test
    public void testFindById_withExistentCategoryId_returnsCorrectlyMappedCategoryView() {
        CategoryView categoryView = this.categoryExtractionService
                .findById(EXISTENT_CATEGORY_ID);

        Assert.assertEquals(EXISTENT_CATEGORY_ID, categoryView.getId());
        Assert.assertEquals(EXISTENT_CATEGORY_NAME, categoryView.getName());
    }
}
