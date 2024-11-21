import jakarta.persistence.*;

import java.util.*;

@Entity
public class Menu {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private int Id;

    public Menu() {}
}
