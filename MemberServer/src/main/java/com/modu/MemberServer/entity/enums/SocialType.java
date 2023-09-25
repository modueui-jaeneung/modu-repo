package com.modu.MemberServer.entity.enums;

public enum SocialType {
    LOCAL {
        @Override
        public String toString() {
            return "LOCAL";
        }
    }, NAVER {
        @Override
        public String toString() {
            return "NAVER";
        }
    }, KAKAO {
        @Override
        public String toString() {
            return "KAKAO";
        }
    }, GOOGLE {
        @Override
        public String toString() {
            return "GOOGLE";
        }
    }
}
