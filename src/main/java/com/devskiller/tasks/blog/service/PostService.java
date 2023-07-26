package com.devskiller.tasks.blog.service;

import com.devskiller.tasks.blog.model.Post;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.devskiller.tasks.blog.model.dto.PostDto;
import com.devskiller.tasks.blog.repository.PostRepository;

@Service
public class PostService {

	private final PostRepository postRepository;
	private ModelMapper mapper;
	public PostService(PostRepository postRepository, ModelMapper mapper) {
		this.postRepository = postRepository;
		this.mapper = mapper;
	}

	public PostDto createPost(PostDto postDto) {
		// convert DTO to entity
		Post post = mapToEntity(postDto);
		Post newPost = postRepository.save(post);

		// convert entity to DTO
		PostDto postResponse = mapToDTO(newPost);
		return postResponse;
	}

	public PostDto getPost(Long id) {
		return postRepository.findById(id)
			.map(post -> new PostDto(post.getTitle(), post.getContent(), post.getCreationDate()))
			.orElse(null);
	}

	private Post mapToEntity(PostDto postDto){
		Post post = mapper.map(postDto, Post.class);
		return post;
	}

	private PostDto mapToDTO(Post post){
		PostDto postDto = mapper.map(post, PostDto.class);
		return postDto;
	}
}
