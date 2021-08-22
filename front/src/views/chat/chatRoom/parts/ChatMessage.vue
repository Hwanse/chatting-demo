<template>
    <div>
        <div v-if="item.messageType === 'JOIN'" class="message text-center">
            {{item.sender}}님이 입장하셨습니다.
        </div>
        <div v-if="item.messageType === 'TALK'" class="message" :class="{own: item.sessionId === mySessionId}">
            <div v-if="item.sessionId !== mySessionId" v-text="item.sender"></div>
            <div class="rounded-lg content">
                <div v-text="item.message"></div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: ["item", "sender", "mySessionId"]
}
</script>