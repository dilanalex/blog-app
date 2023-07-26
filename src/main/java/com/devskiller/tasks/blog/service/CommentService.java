package com.devskiller.tasks.blog.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.devskiller.tasks.blog.exception.ResourceNotFoundException;
import com.devskiller.tasks.blog.model.Comment;
import com.devskiller.tasks.blog.model.Post;
import com.devskiller.tasks.blog.repository.CommentRepository;
import com.devskiller.tasks.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devskiller.tasks.blog.model.dto.CommentDto;
import com.devskiller.tasks.blog.model.dto.NewCommentDto;

@Service
public class CommentService {
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CommentRepository commentRepository;
	public CommentService(PostRepository postRepository, ModelMapper mapper, CommentRepository commentRepository) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.mapper = mapper;
	}

	/**
	 * Returns a list of all comments for a blog post with passed id.
	 *
	 * @param postId id of the post
	 * @return list of comments sorted by creation date descending - most recent first
	 */
	public List<NewCommentDto> getCommentsForPost(Long postId) {
		// retrieve comments by postId
		List<Comment> comments = commentRepository.findByPostId(postId);
		return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
	}

	/**
	 * Creates a new comment
	 *
	 * @param postId id of the post
	 * @param newCommentDto data of new comment
	 * @return id of the created comment
	 *
	 * @throws IllegalArgumentException if postId is null or there is no blog post for passed postId
	 */
	public NewCommentDto addComment(Long postId, NewCommentDto newCommentDto) {
		Comment comment = mapToEntity(newCommentDto);
		long now = System.currentTimeMillis();
		Timestamp sqlTimestamp = new Timestamp(now);
		comment.setTime(sqlTimestamp);
		// retrieve post entity by id
		Post post = postRepository.findById(postId).orElseThrow(
			() -> new ResourceNotFoundException("Post", "id", postId));

		// set post to comment entity
		comment.setPost(post);

		// comment entity to DB
		Comment newComment =  commentRepository.save(comment);

		return mapToDTO(newComment);
	}

	private Comment mapToEntity(NewCommentDto commentDto){
		Comment comment = mapper.map(commentDto, Comment.class);
		return  comment;
	}
	private NewCommentDto mapToDTO(Comment comment){
		NewCommentDto commentDto = mapper.map(comment, NewCommentDto.class);
		return  commentDto;
	}
}
