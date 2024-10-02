package basico.task.management.dto;

import basico.task.management.util.ValidDate;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaskDto {

    @NotNull
    @NotEmpty
    private String type;

    private Long supplierId;

    @NotNull
    private String assetId;

    @NotNull
    private Long createdById;

    private Long referenceId;

    private Long technicalAssignee;

    private String location;

    private String promotionName;

    private String garaje;

    @ValidDate
    private String deliveryDate;

    private String storageRoom;

    private String code;

    private String province;

    private String direction;

    private String description;

    private String city;

    private Long societyId;

    private Long promotionId;

    private String arrOperacion;

    @Temporal(TemporalType.DATE)
    private Date dateExit;

    private String dateSign;


    private String comments;

    @Max(value = 365)
    @Min(value = 1)
    private Integer noOfDays;

    private List<TaskDocumentDto> taskDocumentDtoList;

    private boolean capex;

    private boolean opex;

    public static void main(String[] args) {
        int[] B = new int[]{10, 6, 6, 8};
        int a = check(100, B, 2);
//        System.out.println(a);
        int c[] = new int[]{2, -2, 3, 0, 4, -7};

    }

    public static int check(int X, int[] B, int Z) {
        List<Integer> list = Arrays.stream(B).boxed().collect(Collectors.toList());

        int bytes_downloaded = list.stream().mapToInt(Integer::intValue).sum();
        int bytes_remaining = X - bytes_downloaded;

        if (B.length < Z) {
            return -1;
        }
        if (bytes_remaining == 0) {
            return 0;
        }
        int average_speed = bytes_downloaded - Z;
        return (int) Math.ceil(bytes_remaining / average_speed);

    }

    public static int a(String S, String B[]) {
        int answer = 0;
        int current_sum = 0;
        //Currently there is one empty subarray with sum 0
        int prefixSumCount[] = new int[]{0, 1};
//        for()
        List<Integer> list = Arrays.stream(prefixSumCount).boxed().collect(Collectors.toList());
        for (String list_element : B) {
            current_sum = current_sum + list_element.length();
            if (list.contains(current_sum)) {
                answer = answer + prefixSumCount[current_sum];
            }
            if (!list.contains(current_sum)) {
                prefixSumCount[current_sum] = 1;
            } else {
                prefixSumCount[current_sum] = prefixSumCount[current_sum] + 1;
            }

//            if current_sum not in prefixSumCount:
//            prefixSumCount[current_sum] = 1
//        else:
//            prefixSumCount[current_sum] = prefixSumCount[current_sum] + 1


        }
        if (answer > 1000000000) {
            return -1;
        } else {
            return answer;
        }
    }
}