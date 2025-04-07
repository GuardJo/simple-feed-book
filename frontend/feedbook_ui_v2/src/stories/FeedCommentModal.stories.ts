import {Meta, StoryObj} from "@storybook/react";
import FeedCommentModal from "@/components/FeedCommentModal";
import {fn} from "@storybook/test";

const meta = {
    title: 'Components/FeedCommentModal',
    component: FeedCommentModal,
} satisfies Meta<typeof FeedCommentModal>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        open: true,
        onCloseAction: fn(() => {
            console.log('Closed!')
        }),
        feedId: 1,
    },
}