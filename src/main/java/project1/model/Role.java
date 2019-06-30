package project1.model;

import java.util.Objects;

public class Role {
    private int r_id;
    private String name;
    private boolean can_approve_ri_basic;
    private boolean can_approve_ri_intermediate;
    private boolean can_approve_ri_super;
    private int u_id;

    public Role(int r_id, String name, boolean can_approve_ri_basic, boolean can_approve_ri_intermediate,
            boolean can_approve_ri_super, int u_id) {
        this.r_id = r_id;
        this.name = name;
        this.can_approve_ri_basic = can_approve_ri_basic;
        this.can_approve_ri_intermediate = can_approve_ri_intermediate;
        this.can_approve_ri_super = can_approve_ri_super;
        this.u_id = u_id;
    }

    public int getR_id() {
        return this.r_id;
    }

    public void setR_id(int r_id) {
        this.r_id = r_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCan_approve_ri_basic() {
        return this.can_approve_ri_basic;
    }

    public boolean getCan_approve_ri_basic() {
        return this.can_approve_ri_basic;
    }

    public void setCan_approve_ri_basic(boolean can_approve_ri_basic) {
        this.can_approve_ri_basic = can_approve_ri_basic;
    }

    public boolean isCan_approve_ri_intermediate() {
        return this.can_approve_ri_intermediate;
    }

    public boolean getCan_approve_ri_intermediate() {
        return this.can_approve_ri_intermediate;
    }

    public void setCan_approve_ri_intermediate(boolean can_approve_ri_intermediate) {
        this.can_approve_ri_intermediate = can_approve_ri_intermediate;
    }

    public boolean isCan_approve_ri_super() {
        return this.can_approve_ri_super;
    }

    public boolean getCan_approve_ri_super() {
        return this.can_approve_ri_super;
    }

    public void setCan_approve_ri_super(boolean can_approve_ri_super) {
        this.can_approve_ri_super = can_approve_ri_super;
    }

    public int getU_id() {
        return this.u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Role)) {
            return false;
        }
        Role role = (Role) o;
        return r_id == role.r_id && Objects.equals(name, role.name) && can_approve_ri_basic == role.can_approve_ri_basic
                && can_approve_ri_intermediate == role.can_approve_ri_intermediate
                && can_approve_ri_super == role.can_approve_ri_super && u_id == role.u_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r_id, name, can_approve_ri_basic, can_approve_ri_intermediate, can_approve_ri_super, u_id);
    }

}
