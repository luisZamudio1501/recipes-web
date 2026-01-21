package com.luis.recipes_web.dto.common;

public class SuggestItemDTO {

    private Long id;
    private String label;
    private String extra;

    public SuggestItemDTO() {
    }

    public SuggestItemDTO(Long id, String label, String extra) {
        this.id = id;
        this.label = label;
        this.extra = extra;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getExtra() { return extra; }
    public void setExtra(String extra) { this.extra = extra; }
}
