package com.app.final_project.eventImage;

import com.app.final_project.util.ImgBBUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventImageService {

    @Autowired
    EventImageRepository eventImageRepository;

    public List<EventImage> findAllByEventIdIn(List<Integer> eventIds) {
        return eventImageRepository.findAllByEventIdIn(eventIds);
    }

    public List<String> findAllByEventId(Integer eventId) {
        List<EventImage> eventImages = eventImageRepository.findAllByEventId(eventId);
        return eventImages.stream().map(EventImage::getImageUrl).collect(Collectors.toList());
    }

    @Transactional
    public List<EventImage> saveListEventImage(int eventId, List<MultipartFile> images) {
        List<String> imgUrls = ImgBBUtils.uploadImages(images);
        List<EventImage> eventImages = imgUrls.stream().map(url ->
                EventImage.builder()
                        .imageUrl(url)
                        .eventId(eventId)
                        .build()
        ).toList();
        return eventImageRepository.saveAll(eventImages);
    }

    public void deleteAllByEventId(Integer eventId) {
        eventImageRepository.deleteAllByEventId(eventId);
    }
}
