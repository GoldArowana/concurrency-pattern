package pers.ThreadAlarm;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class AlarmInfo {
    @NonNull
    private String id;

    @NonNull
    public final AlarmType type;

    private String extraInfo;

}
