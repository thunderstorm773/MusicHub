package com.tu.musichub.admin.category.validations;

import com.tu.musichub.admin.category.models.views.CategoryView;
import com.tu.musichub.admin.category.services.CategoryExtractionService;
import com.tu.musichub.staticData.TestConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintValidatorContext;

@RunWith(SpringRunner.class)
@ActiveProfiles(TestConstants.TEST_PROFILE)
@SpringBootTest
public class CategoryValidatorTests {

    private static final String NON_EXISTENT_CATEGORY_NAME = "Rock";
    private static final String EXISTENT_CATEGORY_NAME = "Pop";

    @MockBean
    private CategoryExtractionService categoryExtractionService;

    @Autowired
    private CategoryValidator categoryValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    private CategoryView createTestCategoryView() {
        return new CategoryView();
    }

    @Before
    public void setUp() {
        Mockito.when(this.categoryExtractionService.findByName(NON_EXISTENT_CATEGORY_NAME))
                .thenReturn(null);

        Mockito.when(this.categoryExtractionService.findByName(EXISTENT_CATEGORY_NAME))
                .thenReturn(this.createTestCategoryView());
    }

    @Test
    public void testIsValid_withNonExistentCategoryName_returnsTrue() {
        boolean isValid = this.categoryValidator
                .isValid(NON_EXISTENT_CATEGORY_NAME, this.constraintValidatorContext);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValid_withExistentCategoryName_returnsFalse() {
        boolean isValid = this.categoryValidator
                .isValid(EXISTENT_CATEGORY_NAME, this.constraintValidatorContext);
        Assert.assertFalse(isValid);
    }
}
