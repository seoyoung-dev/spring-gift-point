package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    @Autowired
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> getAllByMemberId(@LoginMember Long memberId,
        @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "id,desc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        Page<WishResponseDto> wishlist = wishService.getWishlist(memberId, pageable);

        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addWishlist(@LoginMember Long memberId,
        @RequestBody @Valid WishRequestDto wishlistRequestDto) {
        wishService.addWishlist(memberId, wishlistRequestDto.getProductId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteWishlist(@LoginMember Long memberId, @RequestBody Long productId) {
        wishService.deleteById(memberId, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}