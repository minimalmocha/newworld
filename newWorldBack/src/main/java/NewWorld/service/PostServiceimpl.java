package NewWorld.service;

import NewWorld.domain.PostLike;
import NewWorld.domain.Post;
import NewWorld.domain.User;
import NewWorld.dto.PostDto;
import NewWorld.exception.CustomError;
import NewWorld.exception.ErrorCode;
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

    @Override
    public Page<Post> getAllPost(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public PostDto getPost(PostDto info) throws CustomError {
        Post post = postRepository.findById(info.getPostId())
                .orElseThrow(() -> new CustomError(ErrorCode.NOT_FOUND));

        post.addview();
        PostDto postDto = PostDto.of(post);

        return postDto;
    }

    @Override
    public PostDto makePost(PostDto postDto) throws CustomError {
        String userNickName = postDto.getNickname();
        User user = userRepository.findByNickname(userNickName)
                .orElseThrow(() -> new CustomError(ErrorCode.USER_NOT_FOUND));

        Post post = Post.of(postDto);
        Post savedPost = postRepository.save(post);

        user.getPostList().add(savedPost);

        return PostDto.of(savedPost);
    }

    @Override
    public PostDto changePost(PostDto postDto) {

        Post post = getPost(postDto.getPostId());

        Post changedPost = post.chagePost(postDto);
        return PostDto.of(changedPost);
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
        boolean check = post.checkLike(user);

        int result = 0;

        if(check) {
            List<PostLike> postLikes = post.minusLike(user);
            if(postLikes == null){
                return 0;
            }
            result = (int) postLikes.stream().count();
        }else if(!check){
            List<PostLike> postLikes = post.addLike(PostLike.of(user));
            result = (int) postLikes.stream().count();
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
