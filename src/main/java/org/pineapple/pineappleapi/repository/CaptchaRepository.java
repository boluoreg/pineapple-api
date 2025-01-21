package org.pineapple.pineappleapi.repository;

import org.pineapple.pineappleapi.entity.Captcha;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepository extends KeyValueRepository<Captcha, String> {
}
