package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.validation.EndAfterStart;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EndAfterStart
public class BookItemRequestDto {

	@NotNull
	private long itemId;

	@FutureOrPresent
	@NotNull
	private LocalDateTime start;

	@Future
	@NotNull
	private LocalDateTime end;
}
