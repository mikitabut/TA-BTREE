package com.bsu.TA;
import java.io.*;

public class Tree {
    private Integer data;
    private Tree left;
    private Tree right;
    private int size;
    private int height;
    private int maxPartpath;
    private Integer sumOfElement;

    public void addElement(Integer elementData) {
        Tree element = new Tree();
        element.data = elementData;
        if (data == null) {
            data = elementData;
            return;
        }
        if (data.compareTo(element.data) > 0) {
            if (left != null) {
                left.addElement(element.data);
            } else {
                left = element;
                size++;
            }
        } else if (data.compareTo(element.data) < 0) {
            if (right != null) {
                right.addElement(element.data);
            } else {
                right = element;
                size++;
            }
        } else {
            return;
        }

    }

    private void leftForwardGoing(FileWriter fileWriter) throws IOException {
        show(fileWriter);
        if (left != null) {
            left.leftForwardGoing(fileWriter);
        }
        if (right != null) {
            right.leftForwardGoing(fileWriter);
        }
    }

    private Tree getMaxPartPath() {
        if (this.left == null && this.right == null) {
            return this;
        } else if (left == null) {
            Tree treeTemp = right.getMaxPartPath();
            return getMaxRoot(this, treeTemp);
        } else if (right == null) {
            Tree treeTemp = left.getMaxPartPath();
            return getMaxRoot(this, treeTemp);
        } else {
            Tree treeMaxLeft = left.getMaxPartPath();
            Tree treeMaxRight = right.getMaxPartPath();
            return getMaxRoot(this, getMaxRoot(treeMaxLeft, treeMaxRight));
        }
    }

    private Tree getMaxRoot(Tree first, Tree second) {
        if (first.maxPartpath > second.maxPartpath) {
            return first;
        } else if (first.maxPartpath < second.maxPartpath) {
            return second;
        } else if (first.sumOfElement > second.sumOfElement) {
            return first;
        } else {
            return second;
        }
    }

    public void setHeightAndMaxPartPathAndSumOfElement() {
        if (this.left == null && this.right == null) {
            this.height = 0;
            this.maxPartpath = 0;
            this.sumOfElement = this.data;
        } else if (this.left == null) {
            right.setHeightAndMaxPartPathAndSumOfElement();
            height = right.height + 1;
            maxPartpath += height;
            sumOfElement = data;
            sumOfElement += right.sumOfElement;
        } else if (this.right == null) {
            left.setHeightAndMaxPartPathAndSumOfElement();
            height = left.height + 1;
            maxPartpath += height;
            sumOfElement = data;
            sumOfElement += left.sumOfElement;
        } else {
            maxPartpath = 2;
            left.setHeightAndMaxPartPathAndSumOfElement();
            right.setHeightAndMaxPartPathAndSumOfElement();
            height = Math.max(left.height, right.height) + 1;
            maxPartpath += left.height + right.height;
            sumOfElement = data;
            sumOfElement += right.sumOfElement + left.sumOfElement;
        }
    }

    private Tree getCentral(int maxPartpath) {
        if (this.maxPartpath == maxPartpath && maxPartpath % 2 != 0) {
            return null;
        }
        if (maxPartpath == height && this.maxPartpath!=maxPartpath) {
            return this;
        }
        if (this.maxPartpath == maxPartpath) {
            if (left != null && right != null) {
                if (left.height <= right.height) {
                    return right.getCentral(right.height - left.height);
                } else {
                    return left.getCentral(left.height - right.height);
                }
            }
        }
        if (left != null && right != null) {
            if (left.height <= right.height) {
                return right.getCentral(maxPartpath - 1);
            } else {
                return left.getCentral(maxPartpath - 1);
            }
        } else if (left != null) {
            return left.getCentral(maxPartpath - 1);
        } else if (right != null) {
            return right.getCentral(maxPartpath - 1);
        } else {
            return null;
        }
    }

    private void doTask1() {
        setHeightAndMaxPartPathAndSumOfElement();
        Tree treePath = getMaxPartPath();
        Tree treeCentral = treePath.getCentral(treePath.maxPartpath);
        if (treePath != treeCentral) {
            if (treeCentral != null) {
                rightDelete(treeCentral.data);
            }
            rightDelete(treePath.data);

        } else {
            rightDelete(treeCentral.data);
        }
        return;
    }

    private void show(FileWriter fileWriter) throws IOException {
        fileWriter.write(data.toString());
        // запись по символам
        fileWriter.append('\n');
    }

    private Tree rightDelete(Integer element) {
        if (data.compareTo(element) > 0) {
            if (left == null) {
                return this;
            }
            left = left.rightDelete(element);
        } else if (data.compareTo(element) < 0) {
            if (right == null) {
                return this;
            }

            right = right.rightDelete(element);
        } else {
            if (right != null && left != null) {
                if (right.left == null) {
                    this.data = right.data;
                    this.right = right.right;
                } else {
                    data = right.getMin();
                }
            } else if (left != null) {
                this.data = left.data;
                this.right = left.right;
                this.left = left.left;
            } else if (right != null) {
                this.data = right.data;
                this.left = right.left;
                this.right = right.right;
            } else {
                return null;
            }
        }
        return this;
    }

    private Integer getMin() {
        if (left == null) {
            return data;
        }
        if (left.left == null) {
            Integer elData = left.data;
            if (left.right != null) {
                left = left.right;
            } else {
                left = null;
            }
            return elData;
        } else {
            return left.getMin();
        }
    }

    public static void main(String[] args) {

        Tree tree = new Tree();
        String text = "Hello world!"; // строка для записи
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream("in.txt")));
            String line;

            while ((line = reader.readLine()) != null) {
                tree.addElement(Integer.parseInt(line));
            }
            tree.doTask1();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("out.txt", false)) {
            // запись всей строки
            if (tree != null) {
                tree.leftForwardGoing(writer);
            }
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

    }
}
