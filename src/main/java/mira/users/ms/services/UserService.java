package mira.users.ms.services;

import lombok.RequiredArgsConstructor;
import mira.users.ms.dto.*;
import mira.users.ms.entity.RoleModel;
import mira.users.ms.entity.UserModel;
import mira.users.ms.exceptions.BadRequestException;
import mira.users.ms.exceptions.NotFoundException;
import mira.users.ms.repositories.RoleRepository;
import mira.users.ms.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    public UserModel findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException(String.format("User with id = %d", userId)));
    }

    public Long newUser(NewUserDto userDto) {
        UserModel user = modelMapper.map(userDto, UserModel.class);
        userRepository.save(user);
        return user.getId();
    }

    public Long saveUser(Long userId, UserUpdateDto userDto) {
        UserModel user = findById(userId);
        user.setEmail(userDto.getEmail());
        user.setLogin(userDto.getLogin());
        userRepository.save(user);
        return user.getId();
    }

    public void deleteUser(Long userId) {
        findById(userId); //check user id
        userRepository.deleteById(userId);
    }

    public Long setPassword(Long userId, SetPasswordDto passwordDto) {
        UserModel user = findById(userId);
        if(!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())){
            throw new BadRequestException("Old password incorrect");
        }
        user.setPassword(passwordDto.getNewPassword());
        userRepository.save(user);
        return user.getId();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleModel> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(
                role.getName())).collect(Collectors.toList());
    }

    public UserModel findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with login '%s' not found", login)));
    }

    public UserDetails getUserDetail(String login) {
        UserModel user = findByLogin(login);
        return new User(user.getLogin(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    public UserModel findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email '%s' not found", email)));

    }

    public Set<RoleDto> getUserRoles(Long userId) {
        UserModel user = findById(userId);
        return user.getRoles().stream()
                .map((p)->modelMapper.map(p, RoleDto.class))
                .collect(Collectors.toSet());
    }

    public Set<RoleDto> setUserRoles(Long userId, List<RoleDto> roles) {
        UserModel user = findById(userId);
        user.getRoles().clear();
        List<RoleModel> roleList = roleRepository.findByNameIn(roles);
        user.getRoles().addAll(roleList);
        userRepository.save(user);
        return user.getRoles().stream()
                .map((p)->modelMapper.map(p, RoleDto.class))
                .collect(Collectors.toSet());
    }

    public Set<RoleDto> deleteUserRole(Long userId, String roleName) {
        UserModel user = findById(userId);
        user.setRoles(user.getRoles().stream()
                .filter((p)->roleName!=p.getName())
                .collect(Collectors.toSet()));
        userRepository.save(user);
        return user.getRoles().stream()
                .map((p)->modelMapper.map(p, RoleDto.class))
                .collect(Collectors.toSet());
    }

    public Set<RoleDto> addUserRole(Long userId, String roleName) {
        UserModel user = findById(userId);
        RoleModel roleList = roleRepository.findByName(roleName)
                .orElseThrow(()->new NotFoundException(String.format("Role with name '%s' not found", roleName)));
        user.setRoles(user.getRoles().stream()
                .filter((p)->roleName!=p.getName())
                .collect(Collectors.toSet()));
        userRepository.save(user);
        return user.getRoles().stream()
                .map((p)->modelMapper.map(p, RoleDto.class))
                .collect(Collectors.toSet());
    }

}
