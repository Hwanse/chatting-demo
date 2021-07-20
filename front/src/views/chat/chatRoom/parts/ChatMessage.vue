<template>
    <div>
        <slot :item="{item}"></slot>
    </div>
</template>

<script>
export default {
    props: ['item']
}
</script>