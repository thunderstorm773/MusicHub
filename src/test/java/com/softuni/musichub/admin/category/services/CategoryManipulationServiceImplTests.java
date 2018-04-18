package com.softuni.musichub.admin.category.services;

import com.softuni.musichub.admin.category.entities.Category;
import com.softuni.musichub.admin.category.models.bindingModels.AddCategory;
import com.softuni.musichub.admin.category.models.bindingModels.EditCategory;
import com.softuni.musichub.admin.category.models.views.CategoryView;
import com.softuni.musichub.admin.category.repositories.CategoryRepository;
import com.softuni.musichub.staticData.Constants;
import com.softuni.musichub.util.MapperUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(Constants.TEST_PROFILE)
public class CategoryManipulationServiceImplTests {

    private static final String TEST_CATEGORY_NAME = "Pop";
    private static final Long TEST_CATEGORY_ID = 1L;
    private static final Long EXISTENCE_CATEGORY_ID = 4L;
    private static final Long NON_EXISTENCE_CATEGORY_ID = 5L;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private MapperUtil mapperUtil;

    @Autowired
    private CategoryManipulationService categoryManipulationService;

    @Captor
    private ArgumentCaptor<Category> categoryArgumentCaptor;

    private AddCategory createAddCategory() {
        AddCategory addCategory = new AddCategory();
        addCategory.setName(TEST_CATEGORY_NAME);
        return addCategory;
    }

    private Category createCategory() {
        Category category = new Category();
        category.setId(TEST_CATEGORY_ID);
        category.setName(TEST_CATEGORY_NAME);
        return category;
    }

    private Category createEditedCategory() {
        Category category = new Category();
        category.setId(EXISTENCE_CATEGORY_ID);
        category.setName(TEST_CATEGORY_NAME);
        return category;
    }

    private EditCategory createEditCategory() {
        EditCategory editCategory = new EditCategory();
        editCategory.setName(TEST_CATEGORY_NAME);
        return editCategory;
    }

    @Before
    public void setUp() {
        Mockito.when(this.categoryRepository.save(this.categoryArgumentCaptor.capture()))
                .thenReturn(this.createCategory());

        Mockito.when(this.categoryRepository.existsById(NON_EXISTENCE_CATEGORY_ID))
                .thenReturn(false);

        Mockito.when(this.categoryRepository.existsById(EXISTENCE_CATEGORY_ID))
                .thenReturn(true);
    }

    @Test
    public void testAddCategory_withAddCategory_shouldInvokeCategoryRepositorySave() {
        this.categoryManipulationService.addCategory(this.createAddCategory());
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .save(this.categoryArgumentCaptor.capture());
    }

    @Test
    public void testAddCategory_withAddCategory_returnsCorrectlyMappedCategoryView() {
        CategoryView categoryView = this.categoryManipulationService
                .addCategory(this.createAddCategory());

        Assert.assertEquals(TEST_CATEGORY_ID, categoryView.getId());
        Assert.assertEquals(TEST_CATEGORY_NAME, categoryView.getName());
    }

    @Test
    public void testDeleteById_withCategoryId_shouldInvokesCategoryRepositoryExistsById() {
        this.categoryManipulationService.deleteById(TEST_CATEGORY_ID);
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .existsById(TEST_CATEGORY_ID);
    }

    @Test
    public void testDeleteById_withNonExistenceId_returnsFalse() {
        boolean isDeleted = this.categoryManipulationService
                .deleteById(NON_EXISTENCE_CATEGORY_ID);
        Assert.assertFalse(isDeleted);
    }

    @Test
    public void testDeleteById_withNonExistenceId_shouldNotInvokesCategoryRepositoryDeleteById() {
        this.categoryManipulationService.deleteById(NON_EXISTENCE_CATEGORY_ID);
        Mockito.verify(this.categoryRepository, Mockito.times(0))
                .deleteById(NON_EXISTENCE_CATEGORY_ID);
    }

    @Test
    public void testDeleteById_withExistenceId_shouldInvokesCategoryRepositoryDeleteById() {
        this.categoryManipulationService.deleteById(EXISTENCE_CATEGORY_ID);
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .deleteById(EXISTENCE_CATEGORY_ID);
    }

    @Test
    public void testDeleteById_withExistenceId_returnsTrue() {
        boolean isDeleted = this.categoryManipulationService
                .deleteById(EXISTENCE_CATEGORY_ID);
        Assert.assertTrue(isDeleted);
    }

    @Test
    public void testEdit_withCategoryId_shouldInvokesCategoryRepositoryExistsById() {
        this.categoryManipulationService.edit(this.createEditCategory(), TEST_CATEGORY_ID);
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .existsById(TEST_CATEGORY_ID);
    }

    @Test
    public void testEdit_withNonExistenceCategoryId_returnsNull() {
        CategoryView editedCategory = this.categoryManipulationService.edit(this.createEditCategory(),
                NON_EXISTENCE_CATEGORY_ID);
        Assert.assertNull(editedCategory);
    }

    @Test
    public void testEdit_withExistenceCategoryId_shouldInvokesCategoryRepositorySave() {
        this.categoryManipulationService.edit(this.createEditCategory(), EXISTENCE_CATEGORY_ID);
        Mockito.verify(this.categoryRepository, Mockito.times(1))
                .save(this.categoryArgumentCaptor.capture());
    }

    @Test
    public void testEdit_withExistenceCategoryId_returnsCorrectlyMappedCategoryView() {
        Mockito.when(this.categoryRepository.save(this.categoryArgumentCaptor.capture()))
                .thenReturn(this.createEditedCategory());
        CategoryView categoryView = this.categoryManipulationService
                .edit(this.createEditCategory(), EXISTENCE_CATEGORY_ID);

        Assert.assertEquals(EXISTENCE_CATEGORY_ID, categoryView.getId());
        Assert.assertEquals(TEST_CATEGORY_NAME, categoryView.getName());
    }
}