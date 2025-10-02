package io.openaev.service;

import io.openaev.database.model.Group;
import io.openaev.database.model.Role;
import io.openaev.database.repository.GroupRepository;
import io.openaev.rest.group.form.GroupCreateInput;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
  private final GroupRepository groupRepository;

  public Group createGroup(GroupCreateInput input) {
    return groupRepository.save(createGroupInner(UUID.randomUUID().toString(), input));
  }

  public Group createGroupWithRole(GroupCreateInput input, List<Role> roles) {
    return createGroupWithRole(UUID.randomUUID().toString(), input, roles);
  }

  public Group createGroupWithRole(
      @NotBlank final String id, GroupCreateInput input, List<Role> roles) {
    Group group = createGroupInner(id, input);
    group.setRoles(roles);
    return groupRepository.save(group);
  }

  private Group createGroupInner(@NotBlank final String id, GroupCreateInput input) {
    Group group = new Group();
    group.setUpdateAttributes(input);
    group.setId(id);
    return group;
  }

  public Optional<Group> findById(@NotBlank final String id) {
    return groupRepository.findById(id);
  }
}
