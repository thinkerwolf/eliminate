package com.thinkerwolf.eliminate.login.dto;

/**
 * Data transfer object
 */
public class LoginUserDto {
    private String username;
    private String password;
    private String platform;
    private String yx;
    private String pic;

    private LoginUserDto() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPlatform() {
        return platform;
    }

    public String getYx() {
        return yx;
    }

    public String getPic() {
        return pic;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String username;
        private String password;
        private String platform;
        private String yx;
        private String pic;

        private Builder() {
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setPlatform(String platform) {
            this.platform = platform;
            return this;
        }

        public Builder setYx(String yx) {
            this.yx = yx;
            return this;
        }

        public Builder setPic(String pic) {
            this.pic = pic;
            return this;
        }

        public LoginUserDto build() {
            LoginUserDto loginUserDto = new LoginUserDto();
            loginUserDto.password = this.password;
            loginUserDto.platform = this.platform;
            loginUserDto.yx = this.yx;
            loginUserDto.username = this.username;
            loginUserDto.pic = this.pic;
            return loginUserDto;
        }
    }
}
