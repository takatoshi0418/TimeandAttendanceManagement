package com.github.takatoshi0418.dto;

import lombok.NonNull;

/** フラッシュメッセージ 
 * フラッシュメッセージは、ユーザーに一時的な通知を表示するためのクラスです。
 * タイプとメッセージの内容を持ち、タイプには優先度とCSSクラスが関連付けられています。 
*/
public record FlashMessage(@NonNull Type type, @NonNull String message) {

    /** フラッシュメッセージのタイプ
     * DANGER: エラーメッセージ（処理を継続することができない場合）
     * WARNING: 警告メッセージ（処理を継続することができるが、注意が必要な場合）
     * SUCCESS: 成功メッセージ（処理が成功した場合）
     * INFO: 情報メッセージ（処理の結果や状況を知らせる場合）
     */
    public enum Type {
        DANGER, WARNING, SUCCESS, INFO;

        /** 優先度 */
        private final int priority;

        /** CSSクラス */
        private final String cssClass;

        /**
         * コンストラクタ
         */
        Type() {
            this.priority = this.ordinal();
            this.cssClass = this.name().toLowerCase();
        }

        /**
         * 優先度を取得する
         * @return 優先度
         */
        public int getPriority() {
            return priority;
        }

        /**
         * CSSクラスを取得する
         * @return CSSクラス
         */
        public String getCssClass() {
            return cssClass;
        }
    }
}
