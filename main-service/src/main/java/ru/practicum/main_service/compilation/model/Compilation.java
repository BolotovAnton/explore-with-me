package ru.practicum.main_service.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_service.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Boolean pinned;

	@Column(nullable = false)
	private String title;

	@ManyToMany
	@JoinTable(name = "compilations_events",
			joinColumns = @JoinColumn(name = "compilation_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
	List<Event> events;
}