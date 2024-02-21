package com.guardjo.feedbook.exception;

import com.guardjo.feedbook.model.domain.BaseEntity;

public class EntityNotFoundException extends RuntimeException {
	public EntityNotFoundException(Class<? extends BaseEntity> entity, long id) {
		super(String.format("Not Found %s, id = %s", entity.getName(), id));
	}

	public EntityNotFoundException(Class<? extends BaseEntity> entity, String key, String value) {
		super(String.format("Not Found %s, %s = %s", entity.getName(), key, value));
	}
}
