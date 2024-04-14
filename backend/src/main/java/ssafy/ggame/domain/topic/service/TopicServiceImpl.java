package ssafy.ggame.domain.topic.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import ssafy.ggame.domain.game.dto.GameCardDto;
import ssafy.ggame.domain.game.dto.GameSaleCardDto;
import ssafy.ggame.domain.game.repository.GameCustomRepository;
import ssafy.ggame.domain.prefer.repository.PreferCustomRepository;
import ssafy.ggame.domain.recommendation.controller.RecommendationController;
import ssafy.ggame.domain.recommendation.service.RecommendationService;
import ssafy.ggame.domain.topic.dto.SaleGameDto;
import ssafy.ggame.domain.topic.dto.TopicNewsResDto;
import ssafy.ggame.global.common.StatusCode;
import ssafy.ggame.global.exception.BaseException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final RecommendationService recommendationService;
    private final PreferCustomRepository preferRepository;
    private final GameCustomRepository gameCustomRepository;
//    private final WebDriver driver;
    //선호 게임 기사 가져오기

    @Override
    public List<TopicNewsResDto> hotTopic(Integer userId) {
        List<String> preferGameNames = preferRepository.findPreferGameNames(userId);
        List<TopicNewsResDto> hotTopicDtoList = new ArrayList<>();
        if (preferGameNames.isEmpty()) {//선호 게임이 없으면
            getFamous(hotTopicDtoList);
        } else {//있으면
            int size = preferGameNames.size();
            int newsSize = size > 10 ? 5 : 10;
            for(int i=0;i<5;i++){
                if(i>=preferGameNames.size()) break;
                String gameName = filterGameName(preferGameNames.get(i));
                getCrawlingData(gameName,newsSize,hotTopicDtoList);
            }
            if(hotTopicDtoList.size()<5){
                getFamous(hotTopicDtoList);
            }
        }
        //날짜기준 내림차순 정렬
        hotTopicDtoList.sort(Comparator.comparing(TopicNewsResDto::getHotTopicDate).reversed());

        return hotTopicDtoList;
    }

    private static String filterGameName(String gameName) {
        if(gameName.contains(" / ")){
            gameName = gameName.split(" / ")[0];
        }
        if(gameName.contains(": ")){
            gameName = gameName.split(": ")[0];
        }
        return gameName.replaceAll("[^a-zA-Z0-9\\s가-힣]", "");
    }

    private void getFamous(List<TopicNewsResDto> hotTopicDtoList) {
        //인기게임 5개 조회해서 가져옴
        List<GameCardDto> recentGame = recommendationService.getRecentPopularGameList();
        List<GameCardDto> gameCardDtos = recentGame.subList(0, 3);
        for(GameCardDto game:gameCardDtos){
            String gameName = filterGameName(game.getGameName());
            getCrawlingData(gameName, 5, hotTopicDtoList);
        }
    }

    @Override
    public List<SaleGameDto> salesInfo(Integer userId) {
        //repo에서 세일하는 게임들을 가져온다.
        Map<Integer, List<GameSaleCardDto>> saleGames = gameCustomRepository.findSaleGames(userId);
        List<SaleGameDto> result = new ArrayList<>();
        //SaleGameDto에 알맞게 담아냄
        saleGames.keySet().forEach(percent ->
                result.add(
                        SaleGameDto.builder()
                                .salePercent(percent)
                                .cardDtoList(saleGames.get(percent))
                                .build()
                )
        );
        return result;
    }

    //크롤링 데이터
    public void getCrawlingData(String keyword, int size, List<TopicNewsResDto> hotTopicDtoList) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        options.addArguments("--headless");
        options.addArguments("--no-sandbox"); // Sandbox 비활성화
        options.addArguments("--disable-dev-shm-usage"); // /dev/shm 파티션 사용 안 함
        options.addArguments("--disable-gpu"); // GPU 가속 사용 안 함
        options.addArguments("--remote-debugging-port=9222"); // 원격 디버깅 포트 설정
        System.out.println(keyword+" 크롬 드라이버 실행전");
        // ChromeDriver 생성
        WebDriver driver = new ChromeDriver(options);
        System.out.println(keyword+" 크롬 드라이버 실행후");
        try {
            //시작 URL
            String URL = "https://www.gamemeca.com/search.php?q=" + keyword;
            driver.get(URL);

            // 크롤링하려는 웹 페이지가 로딩 되는 시간을 기다림
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(50));


            // 게임 정보로 이동
            WebElement elements = driver.findElement(By.cssSelector("#content > div.news-list > div.content-left > ul.list_gamedata.search > li > a"));
            elements.click();

            //새창으로 이동
            changeWindow(driver);

            //뉴스로 이동
            WebElement gameNew = driver.findElement(By.cssSelector("#content > div.content-left > div.db-sub-menu > ul > li:nth-child(2) > a"));
            gameNew.click();


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");

            List<WebElement> news = driver.findElements(By.cssSelector("#content > div.content-left > div.news-list > ul > li"));
            if(size>=news.size()) size = news.size();
            for (int i = 0; i < size; i++) {
                WebElement n = news.get(i);
                String link = n.findElement(By.cssSelector("a")).getAttribute("href");
                String img = n.findElement(By.cssSelector("a > img")).getAttribute("src");
                String title = n.findElement(By.cssSelector("div.cont_thumb > strong > a")).getText();
                String desc = n.findElement(By.cssSelector("div.desc_thumb")).getText();
                String dateString = n.findElement(By.cssSelector("div.day_news")).getText();
                LocalDate date = LocalDate.parse(dateString, formatter);
                TopicNewsResDto hotTopicDto = TopicNewsResDto.builder()
                        .hotTopicLink(link)
                        .hotTopicImg(img)
                        .hotTopicTitle(title)
                        .hotTopicShortDesc(desc)
                        .hotTopicDate(date)
                        .build();
                hotTopicDtoList.add(hotTopicDto);
            }
        } catch (NoSuchElementException e) {
            return; //해당하는 요소들이 없으면 그냥 return
        } catch (Exception e) {
            throw new BaseException(StatusCode.CRAWLING_FAILED);
        }finally {
            driver.quit();
        }

    }

    private void changeWindow(WebDriver driver) {
        // 현재 창의 핸들을 저장
        String currentWindowHandle = driver.getWindowHandle();

        // 현재 창 닫기
        driver.close();

        Set<String> handles = driver.getWindowHandles();

        // 새로운 창으로 전환
        for (String handle : handles) {
            if (!handle.equals(currentWindowHandle)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }
}
