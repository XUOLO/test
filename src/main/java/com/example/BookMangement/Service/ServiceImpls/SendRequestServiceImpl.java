package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.SendRequest;
import com.example.BookMangement.Repository.SendRequestRepository;
import com.example.BookMangement.Service.SendRequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * SendRequestServiceImpl
 *
 * @author xuanl
 * @version 01-00
 * @since 5/31/2024
 */
@Service
public class SendRequestServiceImpl implements SendRequestService {
    @Autowired
    private SendRequestRepository sendRequestRepository;
    @Override
    public BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Page<SendRequest> sendRequestPage;

        sendRequestPage = sendRequestRepository.findAllByKeyword(keyword, pageable);
        BaseRes baseRes = new BaseRes();
        baseRes.setStatus("success");
        baseRes.setCode(200);


        if (sendRequestPage.getTotalElements() == 0) {
            baseRes.setMessage("No records found");
        } else {
            baseRes.setMessage("Successful");
        }

        baseRes.setData(sendRequestPage);

        return baseRes;
    }
}
