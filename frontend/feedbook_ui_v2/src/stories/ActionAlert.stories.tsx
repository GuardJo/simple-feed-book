import {Meta, StoryObj} from "@storybook/react";
import ActionAlert from "@/components/ActionAlert";
import {fn} from "@storybook/test";
import {Button} from "@/components/ui/button";

const meta = {
    title: "Components/ActionAlert",
    component: ActionAlert,
} satisfies Meta<typeof ActionAlert>

export default meta

type Story = StoryObj<typeof meta>

export const FeedRemoveAlert: Story = {
    args: {
        onAction: () => {
            fn()
            console.log('Remove Test')
        },
        title: '피드 삭제',
        description: '피드를 삭제하시겠습니까?',
        actionType: '삭제',
        children: <Button>삭제 버튼</Button>
    }
}

export const FeedSaveAlert: Story = {
    args: {
        onAction: () => {
            fn()
            console.log('Update Test')
        },
        title: '피드 저장',
        description: '피드를 저장하시겠습니까?',
        actionType: '저장',
        children: <Button>저장 버튼</Button>
    }
}