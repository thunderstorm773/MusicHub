package com.softuni.musichub.user.service.impl;

import com.softuni.musichub.user.entity.Role;
import com.softuni.musichub.user.model.viewModel.RoleView;
import com.softuni.musichub.user.repository.RoleRepository;
import com.softuni.musichub.user.service.api.RoleService;
import com.softuni.musichub.utils.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    private final MapperUtil mapperUtil;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public RoleView findByName(String roleName) {
        Role role = this.roleRepository.findByAuthority(roleName);
        RoleView roleView = this.mapperUtil.getModelMapper()
                .map(role, RoleView.class);
        return roleView;
    }

    @Override
    public List<RoleView> findAll() {
        List<Role> roles = this.roleRepository.findAll();
        List<RoleView> roleViews = this.mapperUtil.convertAll(roles, RoleView.class);
        return roleViews;
    }
}
