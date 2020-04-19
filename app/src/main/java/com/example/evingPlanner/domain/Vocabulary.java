package com.example.evingPlanner.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.Setter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Getter
@Setter
@Entity(tableName = "table_vocabulary")
public class Vocabulary {

    @PrimaryKey
    @ColumnInfo(name = "table_vocabulary_id")
    private long id;

    private long parentId;

    private String voca;

    private String translation;

    private String description;

    private int thisCycle;

    private int totalCycle;

    public Vocabulary(long id) {
        this.id = id;
        this.voca = "hello world!";
    }

    @Ignore
    public Vocabulary(String voca, String translation, String description, int totalCycle) {
        checkArgument(totalCycle > 0, "totalCycle should be greater than 0");
        checkNotNull(voca);
        checkNotNull(translation);
        checkNotNull(description);

        this.id = System.nanoTime();
        this.parentId = this.id;
        this.voca = voca;
        this.translation = translation;
        this.description = description;
        this.thisCycle = 1;
        this.totalCycle = totalCycle;
    }

    @Ignore
    public Vocabulary(Vocabulary parentVoca, int thisCycle) {
        checkArgument(thisCycle > 0, "thisCycle should be greater than 0");
        checkNotNull(parentVoca, "parentVoca couldn't be null");

        this.id = System.nanoTime();
        this.parentId = parentVoca.getId();
        this.voca = parentVoca.getVoca();
        this.translation = parentVoca.getTranslation();
        this.description = parentVoca.getDescription();
        this.thisCycle = thisCycle;
        this.totalCycle = parentVoca.totalCycle;
    }

    public List<Vocabulary> generateReplicas(Vocabulary parentVoca, int[] cycle) {
        checkNotNull(parentVoca, "parentVoca couldn't be null");
        checkNotNull(cycle, "cycle couldn't be null");

        List<Vocabulary> replicas = new ArrayList<>();
        replicas.add(parentVoca);

        for (int i=0; i < cycle.length; i++) {
            checkArgument(cycle[i] >= 0, "cycle couldn't be negative");
            replicas.add(new Vocabulary(parentVoca, i + 2));
        }
        return replicas;
    }

    public boolean isParent() {
        return this.parentId == this.id;
    }

}
