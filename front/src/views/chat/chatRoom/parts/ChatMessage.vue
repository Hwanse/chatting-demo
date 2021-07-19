<template>
    <div class="my-2">
        <slot :item="{item}"></slot>
    </div>
</template>

<script>
export default {
    props: ['item']
}
</script>

<style>
.message {
    max-width: 15rem;
    background-color: #ECEFF1;
    padding: 0.5rem;
}
</style>