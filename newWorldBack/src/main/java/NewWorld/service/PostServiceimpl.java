package NewWorld.service;

import NewWorld.domain.PostLike;
import NewWorld.domain.Post;
import NewWorld.domain.User;
import NewWorld.dto.PostDto;
import NewWorld.exception.CustomError;
import NewWorld.exception.ErrorCode;
import NewWorld.repository.LikeRepository;
import NewWorld.repository.PostRepository;
import NewWorld.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceimpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Override
    public Page<Post> getAllPost(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public PostDto getPost(PostDto info) throws CustomError {
        Post post = postRepository.findById(info.getPostId())
                .orElseThrow(() -> new CustomError(ErrorCode.NOT_FOUND));
        int count = likeRepository.countAllByPost(post);
        post.addview();
        PostDto postDto = PostDto.of(post, count);

        return postDto;
    }

    @Override
    public PostDto makePost(PostDto postDto) throws CustomError {
        String userNickName = postDto.getNickname();
        User user = userRepository.findByNickname(userNickName)
                .orElseThrow(() -> new CustomError(ErrorCode.USER_NOT_FOUND));

        Post post = Post.of(postDto);
        Post savedPost = postRepository.save(post);
        int count = likeRepository.countAllByPost(post);
        user.getPostList().add(savedPost);


        return PostDto.of(savedPost, count);
    }

    @Override
    public PostDto changePost(PostDto postDto) {
        Post post = getPost(postDto.getPostId());
        int count = likeRepository.countAllByPost(post);
        Post changedPost = post.chagePost(postDto);

        return PostDto.of(changedPost,count);
    }

    @Override
    public void deletePost(PostDto postDto) {
        Post post = getPost(postDto.getPostId());
        postRepository.delete(post);
    }

    @Override
    public int updateLike(PostDto postDto) throws CustomError {
        User user = userRepository.findByNickname(postDto.getUserNickname())
                .orElseThrow(() -> new CustomError(ErrorCode.USER_NOT_FOUND));
        Post post = postRepository.findById(postDto.getPostId())
                .orElseThrow(() -> new CustomError(ErrorCode.USER_NOT_FOUND));
        PostLike check = likeRepository.findByPostAndUser(post, user);

        int result = 0;

        if(check == null) {
            likeRepository.save(PostLike.of(user, post));
            return 1;

        }else if(check != null){
            likeRepository.deleteByPostAndUser(post, user);
            result = likeRepository.countAllByPost(post);
        }

        return result;
    }


    public Post getPost(Long postId) {
        Optional<Post> byId = postRepository.findById(postId);

        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }
}
