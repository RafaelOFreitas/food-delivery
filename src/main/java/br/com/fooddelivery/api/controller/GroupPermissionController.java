package br.com.fooddelivery.api.controller;

import br.com.fooddelivery.api.dto.output.PermissionOutput;
import br.com.fooddelivery.api.mapper.PermissionMapper;
import br.com.fooddelivery.domain.service.GroupService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/groups/{groupId}/permissions")
public class GroupPermissionController {
    private final GroupService groupService;
    private final PermissionMapper permissionMapper;

    public GroupPermissionController(GroupService groupService, PermissionMapper permissionMapper) {
        this.groupService = groupService;
        this.permissionMapper = permissionMapper;
    }

    @GetMapping
    public ResponseEntity<List<PermissionOutput>> getPermissions(@PathVariable Integer groupId) {
        var group = this.groupService.getGroupById(groupId);

        CacheControl cache = CacheControl.maxAge(20, TimeUnit.SECONDS);

        return ResponseEntity
                .ok()
                .cacheControl(cache)
                .body(this.permissionMapper.toCollectionOutput(group.getPermissions()));
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<?> disassociatePermission(@PathVariable Integer groupId, @PathVariable Integer permissionId) {
        this.groupService.disassociatePermission(groupId, permissionId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<?> associatePermission(@PathVariable Integer groupId, @PathVariable Integer permissionId) {
        this.groupService.associatePermission(groupId, permissionId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
