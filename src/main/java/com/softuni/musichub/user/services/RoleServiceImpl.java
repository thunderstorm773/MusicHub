package com.softuni.musichub.user.services;

import com.softuni.musichub.user.entities.Role;
import com.softuni.musichub.user.models.viewModels.RoleView;
import com.softuni.musichub.user.repositories.RoleRepository;
import com.softuni.musichub.user.services.RoleService;
import com.softuni.musichub.util.MapperUtil;
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
