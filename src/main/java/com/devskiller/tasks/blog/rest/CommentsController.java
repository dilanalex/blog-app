package com.devskiller.tasks.blog.rest;

import com.devskiller.tasks.blog.model.dto.CommentDto;
import com.devskiller.tasks.blog.model.dto.NewCommentDto;
import com.devskiller.tasks.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
public class CommentsController {
	@Autowired
	private CommentService commentService;

	public CommentsController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/posts/{postId}/comments")
	public ResponseEntity<NewCommentDto> createComment(@PathVariable(value = "postId") long postId,
													   @Valid @RequestBody NewCommentDto commentDto){
		return new ResponseEntity<>(commentService.addComment(postId, commentDto), HttpStatus.CREATED);
	}

	@GetMapping("/posts/{postId}/comments")
	public List<NewCommentDto> getCommentsByPostId(@PathVariable(value = "postId") Long postId){
		return commentService.getCommentsForPost(postId);
	}
}
