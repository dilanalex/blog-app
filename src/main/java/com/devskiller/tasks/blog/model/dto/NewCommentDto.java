package com.devskiller.tasks.blog.model.dto;

public record NewCommentDto(Long id, String author, String content) {
	public NewCommentDto(){
		this(0L,"Author","Content");
	}
}
