<template>
    <v-tabs
        background-color="white"
        color="deep-purple accent-4"
        right
    >   
        <v-tab>VOICE</v-tab>
        <v-tab>TEXT</v-tab>
        
        <!-- 
            vuetify tabs 컴포넌트 UI는 tab items 항목들이 클릭 이벤트로 활성화 되었을 때에만 아이템에 속한 컴포넌트를 렌더링한다.
            따라서 의도적으로 미리 하위 컴포넌트들까지 미리 랜더링 시켜놓으려면 eager 속성을 사용해야한다.
         -->
        <v-tab-item :key="1" eager>
            <div class="voice-container">
                <ChatVoiceComponent :roomId="roomId" :inputNickname="inputNickname" />
            </div>
        </v-tab-item>
        <v-tab-item :key="2" eager>
            <ChatTextMessageComponent :roomId="roomId" :inputNickname="inputNickname" />
        </v-tab-item>
    </v-tabs>
</template>

<script>
import ChatTextMessageComponent from "./ChatTextMessageComponent.vue"
import ChatVoiceComponent from "./ChatVoiceComponent.vue"

export default {
    props: ["roomId", "inputNickname"],
    watch: {
        inputNickname(nickname) {
            this.inputNickname = nickname
        }
    },
    components: {
        ChatVoiceComponent,
        ChatTextMessageComponent
    }
}
</script>

<style>
    .voice-container {
        height: calc(100vmin - 7rem);
    }
    .chat-container {
        box-sizing: border-box;
        overflow-y: auto;
        padding: 1%;
        height: calc(100vmin - 13rem);
    }
</style>