package com.example.BookMangement.Service.ServiceImpls;

import com.example.BookMangement.Entity.BaseRes;
import com.example.BookMangement.Entity.Book;
import com.example.BookMangement.Entity.SendRequest;
import com.example.BookMangement.Repository.SendRequestRepository;
import com.example.BookMangement.Service.SendRequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * SendRequestServiceImpl
 *
 * @author xuanl
 * @version 01-00
 * @since 5/31/2024
 */
@Service
@Slf4j
public class SendRequestServiceImpl implements SendRequestService {
    @Autowired
    private SendRequestRepository sendRequestRepository;
    @Override
    public BaseRes getCmbSong(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpSession session) {
        Page<SendRequest> sendRequestPage;
        Long userId = (Long) session.getAttribute("userId");
        sendRequestPage = sendRequestRepository.findSendRequestByUserIdAndKeyword(userId,keyword, pageable);
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

    @Override
    public BaseRes getCmbAdmin(String keyword, Pageable pageable, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
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

    @Override
    public SendRequest getSendRequestById(Long id) {
        try {
            Optional<SendRequest> sendRequest = sendRequestRepository.findById(id);
            return sendRequest.orElseThrow(() -> new RuntimeException("SendRequest not found with id: " + id));
        } catch (Exception ex) {
            log.error("SendRequestServiceImpl_getSendRequestById_Error :", ex);
            return null;
        }
    }


}
