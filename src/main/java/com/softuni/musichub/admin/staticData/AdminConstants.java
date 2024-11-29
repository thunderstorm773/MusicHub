package com.softuni.musichub.admin.staticData;

public class AdminConstants {

    public static final String ALL_USERS_TITLE = "All users";

    public static final String ALL_USERS_VIEW = "admin/user/all";

    public static final String DELETE_USER_TITLE = "Delete user";

    public static final String DELETE_USER_VIEW = "admin/user/delete";

    public static final String DELETE_USER = "deleteUser";

    public static final String EDIT_USER = "editUser";

    public static final String EDIT_USER_TITLE = "Delete user";

    public static final String EDIT_USER_VIEW = "admin/user/edit";

    public static final String USER_CANNOT_BE_DELETED_MESSAGE =
            "You cannot delete yourself, to delete this account please log in " +
                    "with different admin account!";

    public static final int USERS_PER_PAGE = 10;

    public static final int MIN_ROLES_COUNT = 1;

    public static final String NOT_SELECTED_ROLES_MESSAGE =
            "You must select at least 1 role";

    public static final String ALL_USERS_ROUTE = "/admin/users/all";

    public static final String DELETE_USER_BASE_ROUTE = "/admin/users/delete/";

    public static final String EDIT_USER_BASE_ROUTE = "/admin/users/edit/";

    public static final String USER_EDITED_MESSAGE = "User successfully edited!";
}
