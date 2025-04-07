import {Meta, StoryObj} from "@storybook/react";
import FeedCommentForm from "@/components/FeedCommentForm";
import {fn} from "@storybook/test";

const meta = {
    title: 'Components/FeedCommentForm',
    component: FeedCommentForm,
} satisfies Meta<typeof FeedCommentForm>

export default meta

type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        value: '',
        onChangeAction: fn(() => console.log('onChangeAction')),
        onSubmitAction: fn(() => console.log('onSubmitAction'))
    }
}