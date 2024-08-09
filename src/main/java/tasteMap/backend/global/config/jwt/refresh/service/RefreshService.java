package tasteMap.backend.global.config.jwt.refresh.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tasteMap.backend.global.config.jwt.refresh.entity.Refresh;
import tasteMap.backend.global.config.jwt.refresh.repository.RefreshRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshService {
    private final RefreshRepository refreshRepository;
    private final long RefreshTokenRemiteTime = 1000L * 60 * 60 * 24; // 1일

    @Transactional
    public void addRefresh(String username, String refresh) {
        Date date = new Date(System.currentTimeMillis() + RefreshTokenRemiteTime);
        Refresh refreshEntity = Refresh.builder()
            .username(username)
            .refresh(refresh)
            .expiration(date.toString())
            .build();
        refreshRepository.save(refreshEntity);
    }

    @Transactional(readOnly = true)
    public Boolean existsByRefresh(String refresh) {
        return refreshRepository.existsByRefresh(refresh);
    }

    @Transactional(readOnly = true)
    public Boolean existsByUsername(String username) {
        return refreshRepository.existsByUsername(username);
    }

    @Transactional
    public void deleteByRefresh(String refresh) {
        refreshRepository.deleteByRefresh(refresh);
    }

    @Transactional
    public void deleteByUsername(String username) {
        refreshRepository.deleteByUsername(username);
    }
}

